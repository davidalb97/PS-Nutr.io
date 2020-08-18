const { merge } = require('webpack-merge')
const common = require('./webpack.common.js')

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    historyApiFallback: true,
    contentBase: './dist',
    port: 9000,
    proxy: {
      '/api': {
        target: {
          host: "localhost",
          protocol: 'http:',
          port: 8080
        },
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    before: function (app) {
      const morgan = require('morgan')
      app.use(morgan('dev'))
    }
  }
})