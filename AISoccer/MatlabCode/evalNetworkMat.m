function out = evalNetworkMat(xg,yg,xb,yb,x,W,V)

out = zeros(size(x));

for i=1:size(x,1)
    for j=1:size(x,2)
        out(i,j) = evalNetwork([xg;yg;xb;yb;x(i,j);1],W,V);
    end
end

end

