function out = evalNetworkMat(xg,yg,xb,yb,x,y,W,V)

out = zeros(size(x));
if size(x) ~= size(y)
    error('matrix dimensions are not consitent');
end

for i=1:size(x,1)
    for j=1:size(x,2)
        out(i,j) = evalNetwork([xg;yg;xb;yb;x(i,j);atan2(y(i,j)-yb,-xb);1],W,V);
    end
end

end

