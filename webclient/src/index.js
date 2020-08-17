import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import Login from './main/login/Login'
import App from './main/App'

ReactDOM.render(
  <BrowserRouter>
    <Login>
      <App />
    </Login>
  </BrowserRouter>,
  document.getElementById('container')
)
