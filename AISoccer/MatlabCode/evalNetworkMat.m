function out = evalNetworkMat(xg,yg,xb,yb,x,W,V)

out = zeros(size(x));

for i=1:size(x,1)
    for j=1:size(x,2)
        a = xg-x(i,j);
        b = xb-x(i,j);
        if a<0
            a=-a;
            b=-b;
        end
        out(i,j) = evalNetwork([a;yg;b;yb;1],W,V);
    end
end

end

