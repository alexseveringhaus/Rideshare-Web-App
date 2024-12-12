
import React, { useState, useEffect, useRef } from "react";

//helper date formatting function
function formatDate(date){
	let result = date.getUTCFullYear() + "-";
	result += date.getUTCMonth().toString().padStart(2, "0") + "-";
	result += date.getUTCDay().toString().padStart(2, "0") + " ";
	result += date.getUTCHours().toString().padStart(2, "0") + ":";
	result += date.getUTCMinutes().toString().padStart(2, "0") + ":";
	result += date.getUTCSeconds().toString().padStart(2, "0");
	return result;
}

export default function Chat() {
  const [socket, setSocket, socketRef] = useState(null);
  const [sender, setSender] = useState(1);
  const [recipient, setRecipient] = useState("");
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const [convos, setConvos] = useState([]);
  const chatDisplayRef = useRef(null);

  //getting conversations
  useEffect(() => {
    getConvos();
  }, []);

  async function getConvos(){
    const response = await fetch("http://localhost:8050/RideshareApp/get-conversations?sender=" + sender)
    .then((response) => {
      if(!response.ok){
        return Promise.reject();
      }
      return Promise.json();
    })
    .then((response) => {
      return response;
    })
    .catch((err) => {
      console.log(err);
      return [{ senderID: sender, recipientID: 2, recipientUsername: "hi"}];
    });
    setConvos(response);
  }

  async function getMessages(){
    const response = await fetch("http://localhost:8050/RideshareApp/get-messages?sender=" + sender + "&recipient=" + recipient)
    .then((response) => {
      if(!response.ok){
        return Promise.reject();
      }
      return Promise.json();
    })
    .then((response) => {
      return response;
    })
    .catch((err) => {
      console.log(err);
      return [];
    });
    return response;
  }

  const initSocket = (s) => {
    setSocket(s);
    s.addEventListener("message", (data) => {
      setMessages((prevMessages) => [...prevMessages, data]);
      chatDisplayRef.current.scrollTop = chatDisplayRef.current.scrollHeight;
    });
    const newMessages = getMessages();
    setMessages(newMessages);
  }

  const openConversation = (e) => {
    e.preventDefault();
    if(socket){
      socket.close();
    }
    const newSocket = new WebSocket("ws://localhost:8050/RideshareApp/message-socket?sender=" + sender + "&recipient=" + recipient);
    newSocket.onopen = initSocket(newSocket);
  };

  const sendMessage = (e) => {
    e.preventDefault();
    if(recipient && sender && socket){
      const messageObj = {
        message: message,
        senderID: sender,
        recipientID: recipient
      };
      const now = new Date(Date.now());
      const timeStr = formatDate(now);
      messageObj.timestamp = timeStr;
      socket.send(JSON.stringify(messageObj));
    }
  };

  // formatting
  return (
    <main className="min-h-screen bg-white text-black p-4 flex flex-row">
      {/* <form className="w-full max-w-xl mx-auto flex flex-row gap-1" onSubmit={enterRoom}>
        <input 
          type="text" 
          className="flex-grow max-w-[calc(40%-0.25rem)] rounded-lg p-2 text-black border border-gray-300"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Your name" 
          required
        />
        <input 
          type="text" 
          className="flex-grow max-w-[calc(40%-0.25rem)] rounded-lg p-2 text-black border border-gray-300"
          value={room}
          onChange={(e) => setRoom(e.target.value)}
          placeholder="Chat room" 
          required
        />
        <button className="w-[20%] rounded-lg p-2 bg-red-500 text-white">Join</button>
      </form> */}
      <div className = "mx-auto rounded-lg">
        {convos.map((el) => {
          return (
            <div onClick={(e) => {
              setRecipient(el.recipientID); 
              openConversation(e);
            }}><p>{el.recipientUsername}</p></div>
          )
        })}
      </div>
      <div>
        <ul className="w-full max-w-xl mx-auto my-4 bg-gray-100 rounded-lg flex-grow flex flex-col p-2 overflow-auto" ref={chatDisplayRef}>
          {messages.map(({ senderID: senderID, recipientID: recipientID, content: content, timestamp: timestamp }, index) => {
            const formattedTime = new Date(timestamp).toLocaleDateString("en-US", {
              hour: "2-digit",
              minute: "2-digit",
            });
            return (
              <li
                key={index}
                className={`w-[60%] ${senderID === sender ? 'self-start' : 'self-end'} bg-white rounded-lg mb-2 overflow-hidden flex-shrink-0 shadow-sm`}
              >
                <div className={`${ senderID === sender ? 'bg-red-500 text-white' : 'bg-yellow-200 text-black'} p-1 px-2 flex justify-between items-center`}>
                  <span className="text-sm">{formattedTime}</span>
                </div>
                <div className="p-2 text-neutral-800">{content}</div>
              </li>
            );
          })}
        </ul>

        <form className="w-full max-w-xl mx-auto flex flex-row gap-1" onSubmit={sendMessage}>
          <input 
            type="text" 
            className="flex-grow max-w-[calc(80%-0.25rem)] rounded-lg p-2 text-black border border-gray-300"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Your message" 
            required
          />
          <button className="w-[20%] rounded-lg p-2 bg-red-500 text-white">Send</button>
        </form>
      </div>
    </main>
  );
}

