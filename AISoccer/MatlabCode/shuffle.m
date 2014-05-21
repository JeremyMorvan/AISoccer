function [X,T] = shuffle(X1,X2,T1,T2,n)

X = [X1,X2];
T = [T1,T2];
permute = randperm(size(X,2));
X = X(:, permute);
T = T(:, permute);

if nargin>4
    X = X(:,1:n);
    T = T(:,1:n);
end

end

