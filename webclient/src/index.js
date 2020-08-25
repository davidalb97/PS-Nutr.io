import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import 'bootstrap/dist/css/bootstrap.min.css'
import '../resources/main.css'

import Container from 'react-bootstrap/Container'

import RouteRenderer from './main/RouteRenderer'
import TopNavigation from './main/menu/TopNavigation'
import UserProvider from './main/authentication/UserProvider'

ReactDOM.render(
  <BrowserRouter>
    <UserProvider>
      {/* Top menu */}
      <TopNavigation />

      <Container>
        <RouteRenderer />
      </Container>
    </UserProvider>
  </BrowserRouter>,
  document.getElementById('container')
)
