import React, { useState, useReducer, useEffect, useCallback } from 'react'

import InputGroup from 'react-bootstrap/InputGroup'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import useFetch, { FetchStates } from '../../common/useFetch'
import MultiSelect from "react-multi-select-component";

export default function Ingredients({ setCanAdvance, setMeal, meal }) {
    const defaultQuantity = {
        unit: meal.unit || "gr",
        amount: 0,
        carbs: 0
    }

    const [ingredientContext, setIngredientContext] = useState({
        options: meal.ingredientOptions || [],
        request: meal.ingredientOptions ? {} : { url: "http://localhost:9000/api/ingredient", shouldCancel: false },
        selected: meal.ingredients || [],
        onFetch: result => {
            const options = result.map(component => {
                component.value = { ...component.value, userQuantity: component.userQuantity || defaultQuantity }
                return component
            })

            setIngredientContext({ ...ingredientContext, options: options });
            setMeal({ ingredientOptions: options })
        },
        foodMapper: json => json.ingredients
    })

    const [mealContext, setMealContext] = useState({
        options: meal.mealOptions || [],
        request: meal.mealOptions ? {} : { url: "http://localhost:9000/api/meal/suggested", shouldCancel: false },
        selected: meal.meals || [],
        onFetch: result => {
            const options = result.map(component => {
                component.value = { ...component.value, userQuantity: component.userQuantity || defaultQuantity }
                return component
            })

            setMealContext({ ...mealContext, options: options });
            setMeal({ mealOptions: options })
        },
        foodMapper: json => json.meals
    })


    useEffect(() => {
        const canAdvance = ingredientContext.selected.length > 0 || mealContext.selected.length > 0

        setCanAdvance(canAdvance)
        if (canAdvance) {
            setMeal({
                ingredients: ingredientContext.selected,
                meals: mealContext.selected
            })
        }
    }, [ingredientContext, mealContext, setCanAdvance, setMeal])


    return <>
        {/* Ingredients */}
        <Row>
            <Col className="col-2">
                <InputGroup.Append>
                    <InputGroup.Text className="list-cta ingredients-cta justify-content-center">Ingredients</InputGroup.Text>
                </InputGroup.Append>
            </Col>
            <Col className="col-10">
                <FoodList
                    context={ingredientContext}
                    onFoodSelect={list => setIngredientContext({ ...ingredientContext, selected: list })}
                />
            </Col>
        </Row>
        {/* Meals */}
        <Row>
            <Col className="col-2">
                <InputGroup.Append>
                    <InputGroup.Text className="list-cta ingredients-cta justify-content-center">Meals</InputGroup.Text>
                </InputGroup.Append>
            </Col>
            <Col className="col-10">
                <FoodList
                    context={mealContext}
                    onFoodSelect={list => setMealContext({ ...mealContext, selected: list })}
                />
            </Col>
        </Row>
    </>
}

function FoodList({ context, onFoodSelect }) {
    const [fetchState, response, json, error] = useFetch(context.request)

    useEffect(() => {
        if (fetchState === FetchStates.error || !json) return

        const options = context.foodMapper(json).map(food => {
            return {
                value: food,
                label: food.name,
            }
        })

        context.onFetch(options)
    }, [json])

    return <>
        <MultiSelect
            options={context.options}
            value={context.selected}
            onChange={onFoodSelect}
            hasSelectAll={false}
            isLoading={context.options.length <= 0}
            valueRenderer={foodValueRenderer}
            ItemRenderer={foodItemRenderer}
        />
    </>
}

function foodValueRenderer(selected) {
    return selected.length ? `${selected.length} components selected` : "No items selected"
}

function foodItemRenderer({ checked, option, onClick, disabled }) {
    const food = option.value
    const info = food.nutritionalInfo

    return (
        <div
            className={`item-renderer ${disabled && "disabled"}`}
        >
            <input
                type="checkbox"
                onChange={onClick}
                checked={checked}
                tabIndex={-1}
                disabled={disabled}
            />
            <span> {`${info.amount}${info.unit}`} of <strong>{food.name}</strong> with {info.carbs}g of carbs</span>
        </div>
    );
}   