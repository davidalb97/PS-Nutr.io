import React from 'react'
import { Link } from 'react-router-dom'

export default function Sidebar() {
    return <nav className="sidebar">
        <SidebarItemContainer containerName="Meal">
            <SidebarItem buttonName="View your custom meals" path="/meals" />
            <SidebarItem buttonName="Add a custom meal" path="/meals/create" />
        </SidebarItemContainer>

        <SidebarItemContainer containerName="User">
            <SidebarItem buttonName="View user" path="/user" />
            <SidebarItem buttonName="Insulin profiles" path="/user/insulin" />
        </SidebarItemContainer>

        <SidebarItemContainer containerName="Moderation">
        <SidebarItem buttonName="View reports" path="/user" />
        <SidebarItem buttonName="View user" path="/user" />
        <SidebarItem buttonName="Testing purposes" path="/test" />
        </SidebarItemContainer>
    </nav>
}


function SidebarItemContainer({ containerName, children }) {

    return <>
        {containerName}
        <ul>
            {
                React.Children.map(children, (child, idx) => <li id={idx}>{child}</li>)
            }
        </ul>
    </>
}


function SidebarItem({ buttonName, path }) {
    return <Link to={path}> <button>{buttonName}</button> </Link>
}