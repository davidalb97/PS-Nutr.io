import React, { useContext } from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'




export default function RouteRenderer() {

    //TODO Add nav bar for user
    //TODO Add side nav bar
    return <Switch>
        <Route path="/meals">

        </Route>

        <Route path="/user">

        </Route>

        <Route path="/moderation">

        </Route>

        <Route exact path='/'>
            <>
                Homepage
            </>
        </Route>
        
        <Route>
            <Redirect to="/" />
        </Route>
    </Switch>
}