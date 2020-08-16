import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import Login from './main/login/Login'

ReactDOM.render(
  <BrowserRouter>
    <Login>
      Homepage
    </Login>
  </BrowserRouter>,
  document.getElementById('container')
)
