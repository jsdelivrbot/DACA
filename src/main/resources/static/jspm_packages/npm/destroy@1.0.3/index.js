/* */ 
var ReadStream = require('fs').ReadStream;
var Stream = require('stream');
module.exports = function destroy(stream) {
  if (stream instanceof ReadStream) {
    return destroyReadStream(stream);
  }
  if (!(stream instanceof Stream)) {
    return stream;
  }
  if (typeof stream.destroy === 'function') {
    stream.destroy();
  }
  return stream;
};
function destroyReadStream(stream) {
  stream.destroy();
  if (typeof stream.close === 'function') {
    stream.on('open', onopenClose);
  }
  return stream;
}
function onopenClose() {
  if (typeof this.fd === 'number') {
    this.close();
  }
}
