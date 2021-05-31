IF EXISTS ( SELECT * FROM sys.objects WHERE TYPE='TR' AND NAME='TR_TransportOffer_TriggerForWhenAnOfferHasBeenAccepted')
	DROP TRIGGER TR_TransportOffer_TriggerForWhenAnOfferHasBeenAccepted;
GO

CREATE TRIGGER TR_TransportOffer_TriggerForWhenAnOfferHasBeenAccepted
   ON  Paket 
   AFTER UPDATE
AS 
BEGIN
	SET NOCOUNT ON

	DECLARE @CursorForPaket CURSOR
	DECLARE @IdPaket INT

	SET @CursorForPaket = CURSOR FOR
	SELECT IdPaket
	FROM inserted
	WHERE StatusIsporuke = 1

	OPEN @CursorForPaket

	FETCH NEXT FROM @CursorForPaket
	INTO @IdPaket

	WHILE @@FETCH_STATUS = 0
	BEGIN

		DELETE FROM Ponuda
		WHERE IdPaket = @IdPaket

		FETCH NEXT FROM @CursorForPaket
		INTO @IdPaket
	END

	CLOSE @CursorForPaket
	DEALLOCATE @CursorForPaket
END
GO

