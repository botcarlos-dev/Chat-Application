'use strict';

// DOM elements
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

// WebSocket client and user information
var stompClient = null;
var username = null;

// Color options for user avatars
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// Function to establish a WebSocket connection
function connect(event) {
    // Get the username entered by the user
    username = document.querySelector('#name').value.trim();

    if(username) {
        // Hide the username input page and show the chat page
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // Create a WebSocket connection using SockJS
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        // Connect to the WebSocket server
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

// Function called upon successful WebSocket connection
function onConnected() {
    // Subscribe to the public chat topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Send a JOIN message to the server with the user's username
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    );

    // Hide the connection status element
    connectingElement.classList.add('hidden');
}

// Function called upon WebSocket connection error
function onError(error) {
    // Display an error message to the user
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// Function to send a chat message
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        // Create a chat message object
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        // Send the chat message to the server
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// Function called when a message is received from the server
function onMessageReceived(payload) {
    // Parse the message payload
    var message = JSON.parse(payload.body);

    // Create a new message element
    var messageElement = document.createElement('li');

    // Determine the type of message and add appropriate styling
    if(message.type === 'JOIN' || message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + (message.type === 'JOIN' ? ' joined!' : ' left!');
    } else {
        messageElement.classList.add('chat-message');

        // Create an avatar element to represent the message sender
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style.backgroundColor = getAvatarColor(message.sender);

        // Append the avatar element to the message element
        messageElement.appendChild(avatarElement);

        // Create a span element to display the message sender's username
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    // Create a paragraph element to display the message content
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    // Append the text element to the message element
    messageElement.appendChild(textElement);

    // Append the message element to the message area
    messageArea.appendChild(messageElement);
    // Automatically scroll to the bottom of the message area to show the latest message
    messageArea.scrollTop = messageArea.scrollHeight;
}

// Function to generate an avatar color based on the message sender's username
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// Event listeners for the username form and message form
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
