var express = require('express');
var router = express.Router();
var multer = require('multer');
const upload = multer({
  dest: 'uploads/' // this saves your file into a directory called "uploads"
}); 
/* GET home page. */
router.post('/', upload.any(), function(req, res, next) {
  console.log(req.body, 'Body');
  console.log(req.files, 'files');
  res.end();
});

module.exports = router;
