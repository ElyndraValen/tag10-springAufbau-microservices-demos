#!/bin/bash

echo "🚀 Starting all Microservices..."
echo ""
echo "⚠️  Make sure to build first: ./build-all.sh"
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Start Eureka Server
echo -e "${BLUE}🔵 Starting Eureka Server (Port 8761)...${NC}"
cd eureka-server
mvn spring-boot:run > ../logs/eureka.log 2>&1 &
EUREKA_PID=$!
cd ..

echo "   Waiting for Eureka to start..."
sleep 10

# Start User Service
echo -e "${BLUE}🔵 Starting User Service (Port 8081)...${NC}"
cd user-service
mvn spring-boot:run > ../logs/user-service.log 2>&1 &
USER_PID=$!
cd ..

sleep 5

# Start Order Service
echo -e "${BLUE}🔵 Starting Order Service (Port 8082)...${NC}"
cd order-service
mvn spring-boot:run > ../logs/order-service.log 2>&1 &
ORDER_PID=$!
cd ..

sleep 5

# Start API Gateway
echo -e "${BLUE}🔵 Starting API Gateway (Port 8080)...${NC}"
cd api-gateway
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
cd ..

echo ""
echo -e "${GREEN}✅ All services started!${NC}"
echo ""
echo "📊 Service URLs:"
echo "  - Eureka Dashboard: http://localhost:8761"
echo "  - User Service:     http://localhost:8081/api/users"
echo "  - Order Service:    http://localhost:8082/api/orders"
echo "  - API Gateway:      http://localhost:8080/api/users"
echo ""
echo "📝 Logs:"
echo "  - tail -f logs/eureka.log"
echo "  - tail -f logs/user-service.log"
echo "  - tail -f logs/order-service.log"
echo "  - tail -f logs/api-gateway.log"
echo ""
echo "🛑 Stop all services:"
echo "  ./stop-all.sh"
echo ""

# Save PIDs
mkdir -p .pids
echo $EUREKA_PID > .pids/eureka.pid
echo $USER_PID > .pids/user.pid
echo $ORDER_PID > .pids/order.pid
echo $GATEWAY_PID > .pids/gateway.pid
