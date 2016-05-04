program 
	var PREV as int ;
	var RESULT as int ;
	var SUM as int ;
	var I as int ;
	var N as int ;
begin
	PREV := 0 - 1 ;
	RESULT := 1 ;
	N := readint ;
	
	if N > 0 then
		I := 0 ;
		while I <= N do
			SUM := RESULT + PREV ;
			PREV := RESULT ;
			RESULT := SUM ;
			if RESULT != 0 then
				writeint RESULT ;
			end ;
			I := I + 1 ;
		end ;
	else
		writeint 0 - 1 ;
	end ;
	
end 
