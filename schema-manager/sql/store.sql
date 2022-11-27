CREATE TABLE IF NOT EXISTS store (
	store INT,
	storetype TEXT,
	assortment TEXT,
	competitiondistance INT,
	competitionopensincemonth INT,
	competitionopensinceyear INT,
	promo2 INT,
	promo2sinceweek INT,
	promo2sinceyear INT,
	promointerval TEXT,
	PRIMARY KEY ((store), storetype)
);