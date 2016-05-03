program 
	var X as int ;
	var FLAG as bool ;
begin

	FLAG := false ;
	X := readint ;

	if FLAG then
		writeint X ;
	else
		writeint X * 2 ;
	end ;

end 
