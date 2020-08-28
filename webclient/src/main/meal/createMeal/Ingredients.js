import React, { useState, useReducer, useEffect, useCallback } from 'react'

import InputGroup from 'react-bootstrap/InputGroup'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import useFetch, { FetchStates } from '../../common/useFetch'
import MultiSelect from "react-multi-select-component";

export default function Ingredients({ setCanAdvance, setMeal, meal }) {
    const [ingredientContext, setIngredientContext] = useState({
        options: meal.ingredientOptions || [],
        request: meal.ingredientOptions ? {} : { url: "http://localhost:9000/api/ingredient", shouldCancel: false },
        selected: meal.ingredients || [],
        onFetch: result => { setIngredientContext({ ...ingredientContext, options: result }); setMeal({ ingredientOptions: result }) },
        foodMapper: json => json.ingredients
    })

    const [mealContext, setMealContext] = useState({
        options: meal.mealOptions || [],
        request: meal.mealOptions ? {} : { url: "http://localhost:9000/api/meal/suggested", shouldCancel: false },
        selected: meal.meals || [],
        onFetch: result => { setMealContext({ ...mealContext, options: result }); setMeal({ mealOptions: result }) },
        foodMapper: json => json.meals
    })


    useEffect(() => {
        function buildUserQuantity(components) {
            const defaultQuantity = {
                unit: meal.unit,
                amount: 0,
                carbs: 0
            }

            return components.map(component => {
                return {
                    ...component,
                    userQuantity: component.userQuantity || defaultQuantity
                }
            })
        }

        const canAdvance = ingredientContext.selected.length > 0 || mealContext.selected.length > 0

        setCanAdvance(canAdvance)
        if (canAdvance) {
            setMeal({
                ingredients: buildUserQuantity(ingredientContext.selected),
                meals: buildUserQuantity(mealContext.selected)
            })
        }
    }, [ingredientContext, mealContext, setCanAdvance, setMeal])


    return <>
        {/* Ingredients */}
        <Row>
            <Col xs sm md={"auto"}>
                <InputGroup.Append>
                    <InputGroup.Text >Ingredients</InputGroup.Text>
                </InputGroup.Append>
            </Col>
            <Col>
                <FoodList
                    context={ingredientContext}
                    onFoodSelect={list => setIngredientContext({ ...ingredientContext, selected: list })}
                />
            </Col>
        </Row>
        {/* Meals */}
        <Row>
            <Col xs sm md={"auto"}>
                <InputGroup.Append>
                    <InputGroup.Text>Meals</InputGroup.Text>
                </InputGroup.Append>
            </Col>
            <Col >
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