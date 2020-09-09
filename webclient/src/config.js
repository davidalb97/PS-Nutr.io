
//https://webpack.js.org/guides/production/
//Node.js sets NODE_ENV with DefinePlugin
const isDevelopment = process.env.NODE_ENV !== 'production'
const baseReleaseUri = 'https://nutrio-app.herokuapp.com'
const baseDevUri = 'http://localhost:9000'

const baseApiUri = isDevelopment ? `${baseDevUri}/api` : baseReleaseUri
export default baseApiUri
// module.exports = {
//     isDevelopment: isDevelopment,
//     baseDevUri: baseDevUri,
//     baseReleaseUri: baseReleaseUri,
//     baseApiUri: baseApiUri
// }