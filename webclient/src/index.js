import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import 'bootstrap/dist/css/bootstrap.min.css'
import '../resources/main.css'

import RouteRenderer from './main/RouteRenderer'
import TopNavigation from './main/menu/TopNavigation'
import BottomNavigation from './main/menu/BottomNavigation'
import UserProvider from './main/authentication/UserProvider'

ReactDOM.render(
  <BrowserRouter>
    <UserProvider>
      <TopNavigation />
      <RouteRenderer />
      <div className="spacer" />
      <BottomNavigation />
    </UserProvider>
  </BrowserRouter>,
  document.getElementById('container')
)