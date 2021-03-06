# lock 파일 복제 후 , lock-file 기반으로 설치 (옵션 --frozen-lockfile)
FROM node:16.16.0-alpine AS deps
# Check https://github.com/nodejs/docker-node/tree/b4117f9333da4138b03a546ec926ef50a31506c3#nodealpine to understand why libc6-compat might be needed.
RUN apk add --no-cache libc6-compat
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --frozen-lockfile

# deps 에서 설치한 파일중 node_modules만 가져옴
# 옵션 --production devDependencies에 있는 모듈 설치 안함
# 옵션 --ignore-scripts 실행중 다른 스크립트 비활성화
# 옵션 --prefer-offline 네트워크 연결 없이 오프라인으로 install 진행
FROM node:16.16.0-alpine AS builder
WORKDIR /app
COPY . .
COPY --from=deps /app/node_modules ./node_modules
RUN npm run build && npm install --production --ignore-scripts --prefer-offline

# Production image, copy all the files and run next
FROM node:16.16.0-alpine AS runner
WORKDIR /app

ENV NODE_ENV production

# os 사용자 추가 # 추후 수정
RUN addgroup -g 1001 -S nodejs
RUN adduser -S nextjs -u 1001

# builder 에서 복사해야할 파일이 있다면 추가하면 된다. 
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder --chown=nextjs:nodejs /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json

# 추후 수정
USER nextjs

# 외부 포트 사용한다는 알림 없어도 상관없음
EXPOSE 3000

ENV PORT 3000

CMD ["node_modules/.bin/next", "start"]​

# -----
# version 0.0.1
# FROM node:16.16.0

# RUN mkdir -p /usr/src/app
# WORKDIR /usr/src/app

# COPY package*.json ./
# RUN npm install

# COPY . .

# EXPOSE 3000

# CMD ["npm","run","dev"]