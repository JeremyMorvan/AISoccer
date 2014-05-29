function Z = plotZonesNet(x,y,xb,yb,vxb,vyb,net)

X = -103:0.5:103;
Y = -68:0.5:68;
Z = zeros(size(Y,2),size(X,2));
[xx,yy] = meshgrid(X,Y);
nb = size(xx(:),1);

pb = sqrt(vxb^2+vyb^2);

[xr,yr] = toRel(x,y,xb,yb,vxb,vyb);
[xxr,yyr] = toRel(xx(:)',yy(:)',xb,yb,vxb,vyb);
patterns = zeros(5,nb);
patterns(1,:) = pb*ones(1,nb);
patterns(2,:) = xr*ones(1,nb);
patterns(3,:) = yr*ones(1,nb);
patterns(4,:) = xxr;
patterns(5,:) = yyr;
Oout = net(patterns);

s = size(Y,2);
for i=1:size(X,2)
    Z(:,i) = Oout(1,(1+(i-1)*s):(i*s))';
end

contourf(X,Y,Z);
figure;
contourf(X,Y,Z>0);

end

