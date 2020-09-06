
//https://webpack.js.org/guides/production/
//Node.js sets NODE_ENV with DefinePlugin
const isDevelopment = process.env.NODE_ENV !== 'production'
const baseReleaseUri = 'http://localhost:8080'
const baseDevUri = 'http://localhost:9000'
const baseUri = isDevelopment ? `${baseDevUri}/api`: baseReleaseUri

module.exports = {
    isDevelopment: isDevelopment,
    baseDevUri: baseDevUri,
    baseReleaseUri: baseReleaseUri,
    baseApiUri: baseUri
}