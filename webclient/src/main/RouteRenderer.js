import React, { useContext } from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'

import ViewMeals from '../main/meal/ViewMeals'
import CreateMeal from '../main/meal/CreateMeal'
import InsulinProfile from '../main/user/insulinProfile/InsulinProfile'
import AddSuggestedMeal from '../main/moderation/AddSuggestedFood'
import LoginPage from '../main/authentication/login/LoginPage'
import RegisterPage from '../main/authentication/register/RegisterPage'
import UserContext from './authentication/UserContext'
export default function RouteRenderer() {
    const userContext = useContext(UserContext)

    //Disable an unauthenticated user from navigating anywhere
    if (!userContext.user) return <></>

    return <Switch>
        <Route path="/meals/create" >
            <CreateMeal />
        </Route>
        <Route path="/meals">
            <ViewMeals />
        </Route>

        <Route path="/user/insulin">
            <InsulinProfile />
        </Route>
        <Route path="/user">
            User
        </Route>

        <Route path="/moderation/newFood">
            <AddSuggestedMeal />
        </Route>

        <Route path="/login">
            <LoginPage />
        </Route>

        <Route path="/register">
            <RegisterPage />
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