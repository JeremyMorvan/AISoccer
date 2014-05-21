function Z = plotZones(x,y,xt,yt,W,V)

X = -103:0.5:103;
Y = -68:0.5:68;
Z = zeros(size(Y),size(X));
[xx,yy] = meshgrid(X,Y);

nb = size(xx(:));

patterns = zeros(7,nb);
patterns(1,:) = x*ones(1,nb);
patterns(2,:) = y*ones(1,nb);
patterns(3,:) = xx(:)';
patterns(4,:) = yy(:)';
patterns(5,:) = xt*ones(1,nb);
patterns(6,:) = yt*ones(1,nb);
patterns(7,:) = ones(1,nb);
Hin = phiFct(W*patterns);
Hout = [Hin;ones(1,size(patterns,2))];
Oin = V*Hout;
Oout = phiFct(Oin);

s = size(Y);
for i=1:size(X)
    Z(:,i) = Oout(1,(i-1)*s:i*s)';
end

contourf(X,Y,Z);

end

