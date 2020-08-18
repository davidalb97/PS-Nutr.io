import React, { useContext } from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'

import ViewMeals from '../main/meal/ViewMeals'
import CreateMeal from '../main/meal/CreateMeal'

export default function RouteRenderer() {

    //TODO Add nav bar for user
    return <Switch>
        <Route path="/meals/create" >
            <CreateMeal />
        </Route>

        <Route path="/meals">
            <ViewMeals />
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