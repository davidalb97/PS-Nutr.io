import React, { useState, useEffect } from 'react'

import MultiSelect from "react-multi-select-component"
export default function MultiSelectWrapper({ id, options, label = "Select...", controller }) {
    const [selected, setSelected] = useState([])

    useEffect(() => { controller.register({ id: id, initialValue: [], isInitiallyValid: false }) }, [])

    return <MultiSelect
        options={options}
        value={selected}
        onChange={onChange}
        labelledBy={label}
    />

    function onChange(changes) {
        setSelected(changes)
        controller.onChange(id, changes.map(change => change.value), changes.length > 0)
    }
}