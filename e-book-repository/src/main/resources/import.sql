INSERT INTO `ebook`.`category`(`id`,`name`)VALUES(1,"Animals"), (2,"Children"), (3,"Classics"), (4,"Computer science"), (5,"History");

INSERT INTO `ebook`.`language`(`id`,`name`)VALUES(1,"English"),(2,"Serbian");

INSERT INTO `ebook`.`user`(`id`,`enabled`,`firstname`,`lastname`,`password`,`type`,`username`,`category_id`)VALUES(1,true,"Admin","Admin","admin","ADMIN","admin",null),(2,true,"Petar","Petrovic","pera","SUBSCRIBER","pera",4),(3,true,"Mika","Mikic","mika","SUBSCRIBER","mika",1);