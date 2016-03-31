DROP TABLE Product IF EXISTS;
DROP TABLE Bill IF EXISTS;
DROP TABLE BillEntry IF EXISTS;

CREATE TABLE IF NOT EXISTS Product
(
ProductID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
Name VARCHAR(255) NOT NULL,
Price DOUBLE NOT NULL CHECK Price>0,
Stock INT NOT NULL CHECK Stock>=-1,
Image VARCHAR(255) NOT NULL,
is_Deleted BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS Bill
(
InvoiceNumber INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
Issuedate DATE NOT NULL DEFAULT getDate(),
BillRecipient VARCHAR(255) NOT NULL,
BillText VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS BillEntry
(
ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
FKInvoiceNumber INT NOT NULL,
FKProductID INT NOT NULL,
ProductName VARCHAR(255) NOT NULL,
ProductPrice DOUBLE NOT NULL,
Quantity LONG(255) NOT NULL,
FOREIGN KEY(FKInvoiceNumber) REFERENCES Bill(InvoiceNumber),
FOREIGN KEY(FKProductID) REFERENCES Product(ProductID)
);

INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Halfter', 39.99, 22, 'src/res/pictures/1.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Kraftfutter 1kg', 49.99, 13, 'src/res/pictures/2.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Striegel', 14.99, 10, 'src/res/pictures/3.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Sattel', 79.99, 7, 'src/res/pictures/4.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Abschwitzdecke', 129.99, 9, 'src/res/pictures/5.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Zaumzeug', 69.99, 32, 'src/res/pictures/6.png');
INSERT INTO Product (Name, Price, Stock, Image) VALUES ('Mähnenkamm', 34.99, 56, 'src/res/pictures/7.png');

INSERT INTO Bill (Issuedate, BillRecipient, BillText) VALUES ('2016-03-24', 'Nathalie Stürzenbecher', 'Kein Kommentar');
INSERT INTO Bill (Issuedate, BillRecipient, BillText) VALUES ('2016-02-07', 'Robert Barta', 'Kein Kommentar');

INSERT INTO BillEntry (FKInvoiceNumber, FKProductID, ProductName, ProductPrice, Quantity) VALUES (1, 1, 'Halfter', 39.99, 2);
INSERT INTO BillEntry (FKInvoiceNumber, FKProductID, ProductName, ProductPrice, Quantity) VALUES (1, 3, 'Striegel', 14.99, 1);
INSERT INTO BillEntry (FKInvoiceNumber, FKProductID, ProductName, ProductPrice, Quantity) VALUES (1, 7, 'Mähnenkamm', 34.99, 1);
INSERT INTO BillEntry (FKInvoiceNumber, FKProductID, ProductName, ProductPrice, Quantity) VALUES (2, 5, 'Abschwitzdecke', 129.99, 9);

