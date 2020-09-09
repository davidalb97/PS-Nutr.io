const fs = require('fs')

const srcPath = './dist/'
const dstPath = '../httpserver/src/main/resources'
let indexFile = "index.html"
let mainFile = "main.js"
const srcIndexPath = `${srcPath}/${indexFile}`
const dstIndexPath = `${dstPath}/${indexFile}`
const srcMainPath = `${srcPath}/${mainFile}`
const dstMainPath = `${dstPath}/${mainFile}`

const callback = function (err) {
  if (err) throw err
  console.log('Successfully copied files to http server!')
}

fs.copyFile(srcIndexPath, dstIndexPath, callback)
fs.copyFile(srcMainPath, dstMainPath, callback)