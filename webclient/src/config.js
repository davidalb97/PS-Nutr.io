
//https://webpack.js.org/guides/production/
//Node.js sets NODE_ENV with DefinePlugin
const isDevelopment = process.env.NODE_ENV !== 'production'

//Uris - Uncomment as needed if you want to test in a local or deployed server
const baseReleaseUri = 'https://nutrio-app.herokuapp.com/api'
// const baseReleaseUri = 'http://localhost:8080/api'
const baseDevUri = 'http://localhost:9000/development/api'

const baseApiUri = isDevelopment ? baseDevUri : baseReleaseUri

export default baseApiUri