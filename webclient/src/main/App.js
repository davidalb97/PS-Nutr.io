import React from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'
import Sidebar from './menu/Sidebar'
import RouteRenderer from './RouteRenderer'

export default function App() {

    return <>
        <Sidebar />
        <RouteRenderer />
    </>
}