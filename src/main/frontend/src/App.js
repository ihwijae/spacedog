// import logo from './logo.svg';
// import './App.css';
//
// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }
//
// export default App;

// src/main/frontend/src/App.js

import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'; // Switch를 Routes로 변경
import Login from './Login';
import Main from './Main'; // Main 컴포넌트를 import

function App() {
  const [hello, setHello] = useState('')

  useEffect(() => {
    axios.get('/')
        .then(response => setHello(response.data))
        .catch(error => console.log(error))
  }, []);

  return (
      <Router>
          <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/main" element={<Main />} />
          </Routes>
      </Router>
  );
}

export default App;
