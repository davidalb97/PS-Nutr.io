import React, { useContext } from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'

import ViewMeals from '../main/meal/ViewMeals'
import CreateMeal from '../main/meal/CreateMeal'
import LoginPage from '../main/authentication/login/LoginPage'
import RegisterPage from '../main/authentication/register/RegisterPage'

//User
import UserContext from './authentication/UserContext'
import ProfilePage from '../main/user/profile/ProfilePage'
import InsulinProfile from '../main/user/insulinProfile/InsulinProfile'

//Moderation
import AddFoodPage from '../main/moderation/addFood/AddFoodPage'
import Reports from '../main/moderation/reports/ReportPage'
import DetailedReport from '../main/moderation/reports/DetailedReportPage'
export default function RouteRenderer() {
    const userContext = useContext(UserContext)

    if (!userContext.initialized) return <> </>

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
            <ProfilePage />
        </Route>

        <Route path="/moderation/newFood">
            <AddFoodPage />
        </Route>
        <Route path="/moderation/reports/:submissionId">
            <DetailedReport />
        </Route>
        <Route path="/moderation/reports">
            <Reports />
        </Route>

        <Route path="/login">
            <LoginPage />
        </Route>

        <Route path="/register">
            <RegisterPage />
        </Route>

        <Route path="/contactUs">
            Contact us
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