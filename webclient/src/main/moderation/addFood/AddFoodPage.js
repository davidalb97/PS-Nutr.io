import React, { useEffect, useState } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'
import RequestingEntity from '../../common/RequestingEntity'

import AddFoodForm from './AddFoodForm'
import Loading from '../../bootstrap-common/Loading'
export default function AddFoodPage() {
    return <RequestingEntity
        request={{ url: "/cuisine" }}
        onDefault={Loading}
        onSuccess={({ json }) => <AddFoodForm cuisines={json.cuisines} />}
    />
}