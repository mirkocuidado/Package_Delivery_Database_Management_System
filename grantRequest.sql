IF EXISTS ( SELECT * FROM sys.objects WHERE TYPE='P' AND NAME='spAdminAcceptedUsersRequestForBecomingCourier')
	DROP PROCEDURE spAdminAcceptedUsersRequestForBecomingCourier;
GO

CREATE PROCEDURE spAdminAcceptedUsersRequestForBecomingCourier 
	@KorisnickoImeKorisnikaKojiJePoslaoZahtev VARCHAR(100)
AS
BEGIN
	DECLARE @RegBr VARCHAR(100);

	SELECT @RegBr = (SELECT RegistarskiBroj FROM Zahtev WHERE KorisnickoIme=@KorisnickoImeKorisnikaKojiJePoslaoZahtev);

	IF ( NOT EXISTS ( SELECT * FROM Kurir WHERE RegistarskiBroj = @RegBr)) BEGIN
		INSERT INTO Kurir (KorisnickoIme, RegistarskiBroj) VALUES (@KorisnickoImeKorisnikaKojiJePoslaoZahtev, @RegBr);
 
		DELETE FROM Zahtev WHERE KorisnickoIme = @KorisnickoImeKorisnikaKojiJePoslaoZahtev;
	END
END
GO

