function Z = plotZonesDribble(xb,yb,vxb,vyb,W,V)

X = -103:0.5:103;
Y = -68:0.5:68;
Z = zeros(size(Y,2),size(X,2));
[xx,yy] = meshgrid(X,Y);

nb = size(xx(:),1);

pb = sqrt(vxb^2+vyb^2);

[xxr,yyr] = toRel(xx(:)',yy(:)',xb,yb,vxb,vyb);
patterns = zeros(4,nb);
patterns(1,:) = pb*ones(1,nb);
patterns(2,:) = xxr;
patterns(3,:) = yyr;
patterns(4,:) = ones(1,nb);
Oout = evalNetwork(patterns,W,V);

s = size(Y,2);
for i=1:size(X,2)
    Z(:,i) = Oout(1,(1+(i-1)*s):(i*s))';
end

contourf(X,Y,Z);
figure;
contourf(X,Y,Z>0);

end

