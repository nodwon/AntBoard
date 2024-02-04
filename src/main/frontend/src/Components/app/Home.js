/**
 * v0 by Vercel.
 * @see https://v0.dev/t/5nXq7g06lbf
 * Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
 */
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { TableHead, TableRow, TableHeader, TableCell, TableBody, Table } from "@/components/ui/table"

export default function Home() {
    return (

        <div className="flex">
            <div className="flex-1 p-4">
                <h1 className="text-xl font-bold mb-4">수익 인증 게시판</h1>
                <div className="border p-4 mb-4">
                    <Input className="mb-4" placeholder="제목을 입력하세요" />
                    <Textarea className="mb-4" placeholder="본문을 입력하세요" />
                    <Button className="bg-blue-200">이미지 첨부</Button>
                </div>
                <div className="flex justify-between">
                    <Button className="bg-green-200" variant="outline">
                        그린
                    </Button>
                    <Button className="bg-blue-200">저장</Button>
                    <Button className="bg-blue-200">취소</Button>
                </div>
            </div>
            <div className="flex-3">
                <div className="flex justify-between items-center p-4">
                    <h1 className="text-xl font-bold">나의 인증</h1>
                    <Button className="bg-green-500">회원가입</Button>
                </div>
                <div className="flex justify-end space-x-2 p-4">
                    <Button className="bg-green-200">리스트</Button>
                    <Button className="bg-green-200">카드</Button>
                </div>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[50px]">게시글 번호</TableHead>
                            <TableHead>제목</TableHead>
                            <TableHead>닉네임</TableHead>
                            <TableHead>작성일</TableHead>
                            <TableHead>수정일</TableHead>
                            <TableHead className="w-[100px]" />
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        <TableRow>
                            <TableCell className="font-medium">4</TableCell>
                            <TableCell>두번째 테스트 제목</TableCell>
                            <TableCell>네네</TableCell>
                            <TableCell>2024-01-14T06:56:46.415244</TableCell>
                            <TableCell>2024-01-14T06:56:46.415244</TableCell>
                            <TableCell>
                                <Button className="bg-blue-500">수정</Button>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
        </div>
    )
}

