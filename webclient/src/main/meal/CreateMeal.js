import React, { useReducer, useState, useCallback } from 'react'

import ProgressBar from 'react-bootstrap/ProgressBar'
import Button from 'react-bootstrap/Button'

import MealDefinition from './createMeal/MealDefinition'
import Confirmation from './createMeal/Confirmation'
import Finish from './createMeal/Finish'
import Ingredients from './createMeal/Ingredients'
import Portions from './createMeal/Portions'



class Progress {
    constructor(order, label, pageView) {
        if (!React.isValidElement(pageView)) throw Error("Given page view must be a valid JSX element!")
        this.order = order
        this.label = label
        this.pageView = pageView
    }
    static forOrder(idx) {
        return CreationStates[idx]
    }
}

const CreationStates = [
    new Progress(0, "Meal definition", <MealDefinition />),
    new Progress(1, "Ingredients", <Ingredients />),
    new Progress(2, "Portions", <Portions />),
    new Progress(3, "Confirmation", <Confirmation />),
    new Progress(4, "Done", <Finish />)
]

function changeProgress(state, nextState) {
    const next = (nextState instanceof Function) ? nextState(state.order) : nextState
    return Progress.forOrder(next)
}

function changeMeal(meal, newFields) {
    //Generate a new object in order to trigger re-render
    const newMeal = { ...meal }

    Object
        .entries(newFields)
        .forEach(([key, value]) => newMeal[key] = value)

    return newMeal
}

export default function CreateMeal() {
    const [currentProgress, setProgress] = useReducer(changeProgress, Progress.forOrder(0))
    const [canAdvance, setCanAdvance] = useState(false)

    const [meal, addFieldsToMeal] = useReducer(changeMeal, {})

    const onButtonNext = useCallback(() => {
        //TODO Remove meal dependency when no longer needed for console.log()
        console.log(meal)
        setProgress(idx => idx + 1)
        setCanAdvance(false)
    }, [meal, setCanAdvance, setProgress])

    const onSetMeal = useCallback((newFields) => {
        addFieldsToMeal(newFields)
    }, [setCanAdvance, addFieldsToMeal])

    let previous
    let next

    if (currentProgress.order > 0) {
        previous = <Button variant="primary" onClick={() => setProgress(idx => idx - 1)}>Previous</Button>
    }

    if (currentProgress.order < CreationStates.length - 1) {
        next = <Button
            variant="primary"
            onClick={onButtonNext}
            disabled={!canAdvance}
        >
            Next
        </Button>
    }

    return <>
        <ProgressBar
            now={((currentProgress.order + 1) / (CreationStates.length - 1)) * 100}
            label={currentProgress.label}
            max={CreationStates.length * 25}
        />
        <br />

        {React.cloneElement(currentProgress.pageView, {
            setCanAdvance: setCanAdvance,
            setMeal: onSetMeal,
            meal: meal
        })}

        <br />
        {previous}
        {next}
    </>
}

// {currentProgress.pageView({ setCanAdvance: setCanAdvance, setMeal: onSetMeal })}