import React, { useEffect, useState } from 'react'
import Message from './Message'


export default function MessageList({ messages }) {

  const [messagesList, setMessagesList] = useState([]);

  useEffect(()=> {
    setMessagesList(messages);
  })

  return (
    <div>
      {console.log(messagesList)}
      {messagesList.map((m, i) => {
        return <Message index={i} message={m} />
      })}
    </div>
  )
}
