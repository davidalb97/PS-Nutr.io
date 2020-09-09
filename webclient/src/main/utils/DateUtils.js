import { set } from 'date-fns'


const now = new Date()
export const getTodayAtSpecificHour = (hour = 12) => set(now, { hours: hour, minutes: 0, seconds: 0, milliseconds: 0 })