import './App.css';
import MessageList from './features/message/MessageList';
import { useState, useEffect } from 'react';
import axios from 'axios';

let subscription = undefined;

function App() {

  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [subscriptionMessages, setSubsriptionMessages] = useState([]);

  useEffect(() => {
  })

  const getMessages = () => {
    axios.get('http://localhost:8080/api/messages',
      {
        headers: { 'Authorization': 'Basic YWRtaW46YWRtaW4=' }
      })
      .then(success => {
        let data = success.data;
        let messages = data;
        if (messages.length > 0) {
          setSubsriptionMessages(messages);
        }
      })
      .catch(error => console.log(error))
  }

  const addMesageToList = () => {
    if (message) {
      let final_message = {
        id: "",
        text: message
      }
      axios.post('http://localhost:8080/api/message',
        final_message,
        {
          headers: { 'Authorization': 'Basic YWRtaW46YWRtaW4=' }
        })
        .then(success => {
          console.log("message published", success);
          setMessages(old => [...messages, final_message]);
          getMessages();
        }
        )
        .catch(error => console.log(error))
      setMessage('');
    }

  }

  return (
    <div className="container">
      <div className='header'>
        <h1>Chat</h1>
        <p>
          Real time chat using GCP PUB/SUB,
          Spring Boot and
          React
        </p>
      </div>
      <div className='chat-container'>
        <div className='left'>
          <h4>Flow: Spring Boot Endpoint -> publish to topic</h4>
          <div className='chat-body-left'><MessageList messages={messages} /></div>
          <div className='chat-input'>
            <input className='chat-text' value={message} type="text" onChange={(e) => setMessage( e.target.value)} />
            <button className='chat-button' onClick={addMesageToList}>SEND</button>
          </div>
        </div>
        <div className='right'>
          <h4>Flow: Subscribed to topic -> receive message</h4>
          <div className='chat-body-right'>
            <MessageList messages={subscriptionMessages} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
