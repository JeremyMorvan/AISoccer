function Z = plotZones(x,y,xt,yt,W,V)

X = -103:0.5:103;
Y = -68:0.5:68;
Z = zeros(size(Y,2),size(X,2));
[xx,yy] = meshgrid(X,Y);

nb = size(xx(:),1);

patterns = zeros(7,nb);
patterns(3,:) = x*ones(1,nb);
patterns(4,:) = y*ones(1,nb);
patterns(5,:) = xx(:)';
patterns(6,:) = yy(:)';
patterns(1,:) = xt*ones(1,nb);
patterns(2,:) = yt*ones(1,nb);
patterns(7,:) = ones(1,nb);
Hin = phiFct(W*patterns);
Hout = [Hin;ones(1,size(patterns,2))];
Oin = V*Hout;
Oout = phiFct(Oin);

s = size(Y,2);
for i=1:size(X,2)
    Z(:,i) = Oout(1,(1+(i-1)*s):(i*s))';
end

contourf(X,Y,Z);

end

