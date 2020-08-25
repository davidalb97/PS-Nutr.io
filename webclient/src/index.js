import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import 'bootstrap/dist/css/bootstrap.min.css'
import '../resources/main.css'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'


import RouteRenderer from './main/RouteRenderer'
import TopNavigation from './main/menu/TopNavigation'
import BottomNavigation from './main/menu/BottomNavigation'
import UserProvider from './main/authentication/UserProvider'

ReactDOM.render(
  <BrowserRouter>
    <UserProvider>
      {/* Top menu */}
      <Row className="topNav">
        <TopNavigation />
      </Row>

      <RouteRenderer />

      <Row className="bottomNav">
        <BottomNavigation />
      </Row>

    </UserProvider>
  </BrowserRouter>,
  document.getElementById('container')
)
