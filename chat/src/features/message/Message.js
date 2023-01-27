import React, { useEffect } from 'react';
import './message-style.css';


export default function Message({message}) {
  return (
    <div className='message-box'>
      {message.id && <h4>ID: {message.id}</h4>}
      {message.text}
      </div>
  )
}
