import config from '../../config'

export function devUri (url) {
  //HREFs must be changed to match proxy requests
  if (config.isDevelopment) {
    return url.replace(config.baseReleaseUri, config.baseApiUri)
  }
  return url
}