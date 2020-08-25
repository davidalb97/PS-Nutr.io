import React, { useRef, useState, useEffect, useReducer } from 'react'

import Form from 'react-bootstrap/Form'


export default function FormWrapper({ id, type, autoComplete = "on", validators = [], initialValue, controller, displayErrorTooltipOnEmpty = true }) {
    //TODO add a single object, with valid and input object
    const reference = useRef()

    const [context, setContext] = useState({
        value: initialValue,
        valid: isValid(initialValue)
    })

    function isValid(value) {
        return validators.every(validator => validator(value))
    }


    function onChange() {
        if (reference.current) {
            const value = reference.current.value
            const valid = isValid(value)

            setContext({ value: value, valid: valid })
            controller.onChange(id, value, valid)
        }
    }

    useEffect(() => {
        controller.register({ id: id, initialValue: context.value, isInitiallyValid: context.valid })
    }, [])

    return <Form.Control
        controlId={id}
        onChange={onChange}
        ref={reference}
        type={type}
        isValid={context.valid}
        isInvalid={context.value && !context.valid || displayErrorTooltipOnEmpty && !context.valid}
        autoComplete={autoComplete}
    />
}

export function InputController() {
    function reducer(state, { id, value }) {
        const newState = { ...state }
        newState[id] = value
        return newState
    }

    const [body, updateBody] = useReducer(reducer, {})
    const [validMap, updateValidInput] = useReducer(reducer, {})
    const [allFieldsAreValid, setAllFieldsAreValid] = useState(false)

    const [controller] = useState({
        register: ({ id, initialValue, isInitiallyValid = true }) => {
            updateBody({ id: id, value: initialValue })
            updateValidInput({ id: id, value: isInitiallyValid })
        },

        onChange: (id, value, valid = true) => {
            updateBody({ id: id, value: value })
            updateValidInput({ id: id, value: valid })
        }
    })


    useEffect(() => {
        const inputs = Object.values(validMap)

        if (inputs.length <= 0) return

        setAllFieldsAreValid(inputs.every(valid => valid))
    }, [validMap, setAllFieldsAreValid])

    return [controller, body, allFieldsAreValid]
}