import React, { useContext } from 'react'
import { Redirect, Route, Switch } from 'react-router-dom'
import RequireCredentials, { moderator, loggedIn } from './authentication/RequireCredentials'

//Login and register
import LoginPage from '../main/authentication/login/LoginPage'
import RegisterPage from '../main/authentication/register/RegisterPage'

//User
import UserContext from './authentication/UserContext'
import ProfilePage from '../main/user/profile/ProfilePage'
import InsulinProfile from '../main/user/insulinProfile/InsulinProfile'

//Meals
import ViewMeals from '../main/meal/ViewMeals'
import CreateMeal from '../main/meal/CreateMeal'

//Moderation
import AddFoodPage from '../main/moderation/addFood/AddFoodPage'
import Reports from '../main/moderation/reports/ReportPage'
import DetailedReport from '../main/moderation/reports/DetailedReportPage'
export default function RouteRenderer() {
    const userContext = useContext(UserContext)

    if (!userContext.initialized) return <> </>

    return <Switch>
        <Route path="/meals/create" >
            <RequireCredentials requiredCredentials={loggedIn}>
                <CreateMeal />
            </RequireCredentials>
        </Route>
        <Route path="/meals">
            <RequireCredentials requiredCredentials={loggedIn}>
                <ViewMeals />
            </RequireCredentials>
        </Route>

        <Route path="/user/insulin">
            <RequireCredentials requiredCredentials={loggedIn}>
                <InsulinProfile />
            </RequireCredentials>
        </Route>
        <Route path="/user">
            <RequireCredentials requiredCredentials={loggedIn}>
                <ProfilePage />
            </RequireCredentials>
        </Route>

        <Route path="/moderation/newFood">
            <RequireCredentials requiredCredentials={moderator}>
                <AddFoodPage />
            </RequireCredentials>
        </Route>
        <Route path="/moderation/reports/:submissionId">
            <RequireCredentials requiredCredentials={moderator}>
                <DetailedReport />
            </RequireCredentials>
        </Route>
        <Route path="/moderation/reports">
            <RequireCredentials requiredCredentials={moderator}>
                <Reports />
            </RequireCredentials>
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