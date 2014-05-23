function out = evalNetwork(patterns,W,V)

Hin = phiFct(W*patterns);
Hout = [Hin;ones(1,size(patterns,2))];
Oin = V*Hout;
out = phiFct(Oin);

end

