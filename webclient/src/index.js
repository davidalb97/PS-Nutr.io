import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import 'bootstrap/dist/css/bootstrap.min.css';
import '../resources/main.css'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'

import RouteRenderer from './main/RouteRenderer'
import TopNavigation from './main/menu/TopNavigation'
import UserProvider from './main/authentication/UserProvider'

ReactDOM.render(
  <BrowserRouter>
    <UserProvider>
      {/* Top menu */}
      <Container>
        <TopNavigation />

        {/* Body of the page */}
        <RouteRenderer />
      </Container>

    </UserProvider>
  </BrowserRouter>,
  document.getElementById('container')
)
