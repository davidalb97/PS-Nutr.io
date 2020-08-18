import React from 'react'

export default function CreateMeal() {
    const request = {
        url: "http://localhost:8080/api/meals",
        method: "POST"
    }

    return <RequestingEntity
        request={request}
    />
}