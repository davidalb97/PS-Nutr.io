import React from 'react'

import RequestingEntity from '../common/RequestingEntity'

export default function ViewMeals() {
    const request = {
        url: "http://localhost:9000/api/meal",
    }


    return <>View custom meals</>
    return <RequestingEntity
        request={request}
        onSuccess={listMeals}
        onLoad={handleLoad}
    />
}

function listMeals({ json }) {
    return <> Listing meals</>
}

function handleLoad() {
    return <> Loading... </>
}