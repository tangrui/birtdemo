http://www.birt-exchange.com/devshare/designing-birt-reports/324-birt-dynamic-image-examples/

Example report designs that dyamically changes the image.
Two different approaches for getting dynamic images into a BIRT report design.  One approach use a script on the image onRender event to change the name of the image at runtime:
if(row["CREDITLIMIT"] < 25000 )
    this.file = this.file.replace("happy", "sad");


...and the other approach uses a computed column on the dataset to dynamically change the location of the image:
if (row["CREDITLIMIT"] <= 0) {
  "down.jpg"
} else {
  "up.jpg"
}

Both approaches pick up the image from a relative URL inside the same project.  These examples were created with BIRT 2.2.